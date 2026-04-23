function onRandomClick() {
    let result = prompt("Type the number of random songs you wish to practice");
    const isNumeric = (string) => Number.isFinite(+string)
    if (isNumeric(result)) {
        //call BE
    } else {
        alert("Please enter a valid number");
    }
}