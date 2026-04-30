function onRandomClick() {
    let numberOfRandomSongs = prompt("Type the number of random songs you wish to practice");
    const isNumeric = (string) => Number.isFinite(+string)
    if (isNumeric(numberOfRandomSongs)) {
        document.getElementById('numberOfRandomSongs').value = numberOfRandomSongs;
        document.getElementById('getRandomSongs').submit();
    } else {
        alert("Please enter a valid number");
    }
}