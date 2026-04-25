function onRandomClick() {
    let numberOfRandomSongs = prompt("Type the number of random songs you wish to practice");
    const isNumeric = (string) => Number.isFinite(+string)
    if (isNumeric(numberOfRandomSongs)) {
        //call BE
    } else {
        alert("Please enter a valid number");
    }
}