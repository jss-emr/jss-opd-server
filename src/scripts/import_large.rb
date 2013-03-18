require 'rubygems'
require 'active_record'

ActiveRecord::Base.establish_connection(
  :adapter => "mysql",
  :host => "localhost",
  :username => "root",
  :password => "password",
  :database => "openmrs_concepts_dump_large")

class Concept < ActiveRecord::Base
  self.table_name = "concept"
end

class ConceptAnswer < ActiveRecord::Base
  self.table_name = "concept_answer"
end

class PrototypeConcept < ActiveRecord::Base
  self.table_name = "prototype_concept"
end


@history = [1, 4, 5, 7, 11, 12, 13, 20, 22]
@diagnosis = [4]
@instructions = [1, 2, 22]


@sql = <<-STR
select 
    concept.concept_id as concept_id,
    concept_name.name as concept_name,
    concept_datatype.name as datatype,
    concept_class.name as class_name
  from concept
  left join concept_name on concept_name.concept_id = concept.concept_id and concept_name.locale="en"
  left join concept_datatype on concept_datatype.concept_datatype_id = concept.datatype_id
  left join concept_class on concept_class.concept_class_id = concept.class_id
  where
    concept.retired = 0
    and concept.class_id in (?)
STR

@answer_sql = <<-STR
  select concept_answer.concept_id as concept_id, 
    cn1.name as answer, 
    cn2.name as cname
  from concept_answer
  left join concept_name as cn1 on cn1.concept_id = concept_answer.answer_concept and cn1.locale="en" and cn1.concept_name_type = "FULLY_SPECIFIED"
  left join concept_name as cn2 on cn2.concept_id = concept_answer.concept_id and cn2.locale="en" and cn2.concept_name_type = "FULLY_SPECIFIED"
  where
    cn1.locale="en"
    and cn2.locale="en"
    and concept_answer.concept_id = ?;
STR

def putjson(ids, filename, include_props = true)
  json = Concept.find_by_sql([@sql, ids]).map do |concept|
    datatype_prop = {}

    if(concept.datatype == "Coded")
      answers = ConceptAnswer.find_by_sql([@answer_sql, concept.concept_id])
      datatype_prop.merge!(:answers => answers.collect {|c| {:name => c.answer}})
    end

    datatype = include_props ? concept.datatype : "N/A"

    {
      :name => concept.concept_name,
      :id => concept.concept_id,
      :conceptClass => concept.class_name,
      :properties => {:datatype => {:name => datatype, :properties => datatype_prop}}
    }.to_json + ",\n"
  end

  File.open(filename, "w") do |f|
    f.puts "["
    f.puts json
    f.puts "]"
  end
end


putjson(@history, "history.json")
puts "history done"

putjson(@instructions, "instructions.json", false)
puts "instructions done"

putjson(@diagnosis, "diagnosis.json")
puts "diagnosis done"

# x = Concept.find_by_sql(sql).map do |concept|
#   datatype_prop = {}

#   if(concept.datatype == "Coded")
#     answers = ConceptAnswer.find_by_sql([answer_sql, concept.concept_id])
#     datatype_prop.merge!(:answers => answers.collect {|c| {:name => c.answer}})
#   end

#   {
#     :name => concept.concept_name,
#     :id => concept.concept_id,
#     :conceptClass => concept.class_name,
#     :properties => {:datatype => {:name => concept.datatype, :properties => datatype_prop}}
#   }.to_json + ",\n"
# end

# puts x