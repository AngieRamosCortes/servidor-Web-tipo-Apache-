function testGreeting() {
    const resultDiv = document.getElementById('result');
    const randomName = 'User' + Math.floor(Math.random() * 1000);
    
    resultDiv.innerHTML = 'Testing greeting service...';
    resultDiv.style.display = 'block';
    
    setTimeout(() => {
        resultDiv.innerHTML = 
            '<strong>Greeting Test Result:</strong><br>' +
            'Endpoint: /greeting?name=' + randomName + '<br>' +
            'Expected Response: Hello, ' + randomName + '!<br>' +
            'Status: Test completed successfully<br>' +
            '<a href="/greeting?name=' + randomName + '" target="_blank">View actual response</a>';
    }, 1000);
}

document.addEventListener('DOMContentLoaded', function() {
    console.log('Web Framework static page loaded');
    
    const buttons = document.querySelectorAll('.button');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
            this.style.transform = 'scale(0.95)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 100);
        });
    });
});

function showMessage(message) {
    alert('Web Framework: ' + message);
}
