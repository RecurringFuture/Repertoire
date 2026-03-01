function addRowHandlers() {
    var table = document.getElementById("tableId");
    // Add a check to ensure the table element exists before proceeding
    if (table) { // Check if table is not null
        var rows = table.getElementsByTagName("tr");
        for (i = 0; i < rows.length; i++) {
            var currentRow = table.rows[i];
            var createClickHandler =
                function(row)
                {
                    return function() {
                        var cell = row.getElementsByTagName("td")[0];
                        // Add a check for 'cell' as well
                        if (cell) {
                            var id = cell.innerHTML;
                            alert("id:" + id);
                        } else {
                            console.warn("No 'td' found in row for click handler.");
                        }
                    };
                };

            currentRow.onclick = createClickHandler(currentRow);
        }
    } else {
        console.error("Table with ID 'tableId' not found.");
    }
}

// Assign the function reference to window.onload, so it runs after the DOM is fully loaded.
window.onload = addRowHandlers;
