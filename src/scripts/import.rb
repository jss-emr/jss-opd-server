require 'rubygems'
require 'active_record'

ActiveRecord::Base.establish_connection(
  :adapter => "mysql",
  :host => "localhost",
  :username => "root",
  :password => "<password>",
  :database => "<database>")

class Concept < ActiveRecord::Base
  self.table_name = "concept"
end

sql = <<-STR
  select 
      concept.concept_id as concept_id,
      concept_name.name as name,
      answer.answer as answer_name,
      concept_datatype.name as datatype,
      concept_class.name as class_name
    from concept
    left join concept_name on concept_name.concept_id = concept.concept_id and concept_name.concept_name_type = "FULLY_SPECIFIED" and concept_name.locale="en"
    left join concept_datatype on concept_datatype.concept_datatype_id = concept.datatype_id
    left join concept_class on concept_class.concept_class_id = concept.class_id
    left join (select concept_answer.concept_id, 
            answer_concept,
            cn1.name as answer, 
            cn2.name as cname
          from concept_answer
          left join concept_name as cn1 on cn1.concept_id = concept_answer.answer_concept and cn1.concept_name_type = "FULLY_SPECIFIED"
          left join concept_name as cn2 on cn2.concept_id = concept_answer.concept_id and cn2.concept_name_type = "FULLY_SPECIFIED"
          where
            cn1.locale="en"
            and cn2.locale="en")
      as answer on answer.concept_id = concept.concept_id
    where
      concept.retired = 0
      and class_id = 1
      and concept.concept_id not in (select answer_concept from concept_answer)
STR

x = Concept.find_by_sql(sql).group_by(&:name).map do |name, concepts|
  datatype_prop = {}
  datatype_prop.merge!(:answers => concepts.collect {|c| {:name => c.answer_name}}) if concepts.length > 1
  {
    :name => name,
    :id => concepts[0].concept_id,
    :conceptClass => concepts[0].class_name,
    :properties => {:datatype => {:name => concepts[0].datatype, :properties => datatype_prop}}
  }.to_json + ",\n"
end

puts x