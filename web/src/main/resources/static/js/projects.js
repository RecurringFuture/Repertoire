let selectedSongId = null;
let selectedSide = null;

function selectRow(row, side) {
    // Deselect all
    document.querySelectorAll('tr').forEach(r => r.classList.remove('selected-row'));

    // Select current
    row.classList.add('selected-row');
    selectedSongId = row.getAttribute('data-id');
    selectedSide = side;

    console.log("Selected song ID:", selectedSongId, "on side:", selectedSide);
}

function moveLeft() {
    if (selectedSide === 'right' && selectedSongId) {
        document.getElementById('addSongId').value = selectedSongId;
        document.getElementById('addSongForm').submit();
    } else {
        alert("Please select a song from the 'Available' list first.");
    }
}

function moveRight() {
    if (selectedSide === 'left' && selectedSongId) {
        document.getElementById('removeSongId').value = selectedSongId;
        document.getElementById('removeSongForm').submit();
    } else {
        alert("Please select a song from the 'In Project' list first.");
    }
}