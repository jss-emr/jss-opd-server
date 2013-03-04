<html>
<body>
    <h2>Create new Concepts</h2>
    <form action="concept/create" method="post">
        <div>Name: <input type="text" name="name"><div>
        <div>Category: <select name="type">
            <option value="Examination">Examination</option>
            <option value="Diagnosis">Diagnosis</option>
            <option value="Medicine">Medicine</option>
            <option value="Instruction">Instruction</option>
            <option value="Concept">Concept</option>
        </select></div>
        <div>Json: <textarea name="json" rows="10" cols="100"></textarea></div>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
