let filterKey;
let filterState;
let filterTuning;
let filterCapo;

function filter() {
    filterKey = document.getElementById('keyFilterId').value;
    filterState = document.getElementById('stateFilterId').value;
    filterTuning = document.getElementById('tuningFilterId').value;
    filterCapo = document.getElementById('capoFilterId').value;

    console.log("Filter 2: " + filterKey + "");
}