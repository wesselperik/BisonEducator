function getGrades() {
    var elements = document.getElementsByClassName('grade');
    var result = '';

    for (var i = 0; i < elements.length; i++) {
        result += elements[i].value;
    }
    return result;
}