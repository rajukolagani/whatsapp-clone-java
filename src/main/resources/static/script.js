let currentWaId = null; // NEW: To keep track of the currently open chat

document.addEventListener('DOMContentLoaded', function() {
    fetchConversations();
});

// Fetches the list of conversations on the left
async function fetchConversations() {
    // ... (This function remains the same as before)
    try {
        const response = await fetch('http://localhost:8080/api/conversations');
        const conversations = await response.json();
        const chatList = document.getElementById('chat-list');

        while (chatList.children.length > 1) {
            chatList.removeChild(chatList.lastChild);
        }

        conversations.forEach(convo => {
            const chatItem = document.createElement('div');
            chatItem.className = 'chat-item';
            chatItem.innerHTML = `
                <div class="chat-info">
                    <div class="chat-name">${convo.name}</div>
                    <div class="chat-last-message">${convo.lastMessage}</div>
                </div>
            `;
            
            chatItem.addEventListener('click', () => loadMessages(convo.waId, convo.name));
            chatList.appendChild(chatItem);
        });

    } catch (error) {
        console.error('Failed to fetch conversations:', error);
    }
}

// Fetches and displays messages for a specific chat
async function loadMessages(waId, name) {
    currentWaId = waId; // NEW: Set the current waId
    try {
        const response = await fetch(`http://localhost:8080/api/messages/${waId}`);
        const messages = await response.json();
        const chatWindow = document.getElementById('chat-window');
        chatWindow.innerHTML = ''; 

        // Add Header
        const chatHeader = document.createElement('div');
        chatHeader.className = 'chat-window-header';
        chatHeader.innerText = name;
        chatWindow.appendChild(chatHeader);

        // Add Messages Container
        const messagesContainer = document.createElement('div');
        messagesContainer.className = 'messages-container';
        chatWindow.appendChild(messagesContainer);

        messages.forEach(msg => {
            appendMessage(msg.body, messagesContainer); // Use a helper function
        });

        // NEW: Add the message form at the bottom
        const messageForm = document.createElement('form');
        messageForm.className = 'message-form';
        messageForm.innerHTML = `
            <input type="text" id="message-input" placeholder="Type a message">
            <button type="submit">Send</button>
        `;
        chatWindow.appendChild(messageForm);

        // NEW: Add listener for the form submission
        messageForm.addEventListener('submit', sendMessage);

    } catch (error) {
        console.error(`Failed to fetch messages for ${waId}:`, error);
    }
}

// --- NEW: This function handles sending a new message ---
async function sendMessage(event) {
    event.preventDefault(); // Stop the page from reloading

    const messageInput = document.getElementById('message-input');
    const messageBody = messageInput.value.trim();

    if (messageBody === '' || !currentWaId) {
        return; // Don't send empty messages or if no chat is open
    }

    // Instantly add the message to the UI for a fast feel
    const messagesContainer = document.querySelector('.messages-container');
    appendMessage(messageBody, messagesContainer);

    // Create the message object to send to the backend
    const message = {
        body: messageBody,
        waId: currentWaId,
        name: document.querySelector('.chat-window-header').innerText // Get name from header
    };

    // Send the message to the backend
    try {
        await fetch('http://localhost:8080/api/messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(message),
        });
    } catch (error) {
        console.error('Failed to send message:', error);
    }

    messageInput.value = ''; // Clear the input box
}

// --- NEW: Helper function to create and add a message bubble ---
function appendMessage(body, container) {
    const messageBubble = document.createElement('div');
    messageBubble.className = 'message-bubble sent'; // 'sent' class for styling our own messages
    messageBubble.innerText = body;
    container.appendChild(messageBubble);
    container.scrollTop = container.scrollHeight; // Scroll to the bottom
}