/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function songsFunction() {
    document.getElementById("songsDropdown").classList.toggle("show");
}

function showFunction() {
    document.getElementById("practiceDropdown").classList.toggle("show");
}

document.getElementById("songsButton").addEventListener("mouseout", (event) => {
    if (!e.target.matches('.dropbtn')) {
        var songsDropdown = document.getElementById("songsDropdown");
        if (songsDropdown.classList.contains('show')) {
            songsDropdown.classList.remove('show');
        }
    }
})

// Close the dropdown if the user clicks outside of it
window.onclick = function(e) {
    if (!e.target.matches('.dropbtn')) {
        var songsDropdown = document.getElementById("songsDropdown");
        if (songsDropdown.classList.contains('show')) {
            songsDropdown.classList.remove('show');
        }
    }
}

// function myFunction() {
//     document.getElementById("myDropdown").classList.toggle("show");
// }

// // Close the dropdown if the user clicks outside of it
// window.onclick = function(e) {
//     if (!e.target.matches('.dropbtn')) {
//         var myDropdown = document.getElementById("myDropdown");
//         if (myDropdown.classList.contains('show')) {
//             myDropdown.classList.remove('show');
//         }
//     }
// }