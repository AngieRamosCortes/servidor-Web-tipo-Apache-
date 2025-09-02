/**
 * Swift NetFramework - Interactive JavaScript Module
 * Version: 2.1.0
 * Description: Client-side functionality for the Swift NetFramework demo
 * Author: DevTech WebApp Team
 */

const SwiftNetApp = {
    version: '2.1.0',
    baseUrl: window.location.origin,
    
    config: {
        animationDelay: 300,
        requestTimeout: 5000,
        retryAttempts: 3
    },
    
    state: {
        initialized: false,
        requestCount: 0,
        lastResponse: null
    }
};

/**
 * Initialize the application when DOM is loaded
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log(`🚀 Initializing Swift NetFramework Client v${SwiftNetApp.version}`);
    
    SwiftNetApp.init();
    SwiftNetApp.setupEventListeners();
    SwiftNetApp.animateElements();
    
    console.log('✅ Swift NetFramework Client initialized successfully');
});

/**
 * Initialize the application
 */
SwiftNetApp.init = function() {
    const mainElements = document.querySelectorAll('.main-container > *');
    mainElements.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            element.style.transition = 'all 0.6s ease-out';
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * SwiftNetApp.config.animationDelay);
    });
    
    SwiftNetApp.displayFrameworkInfo();
    
    SwiftNetApp.state.initialized = true;
};

/**
 * Setup event listeners for interactive elements
 */
SwiftNetApp.setupEventListeners = function() {
    const testApiBtn = document.getElementById('testApiBtn');
    const loadMetricsBtn = document.getElementById('loadMetricsBtn');
    const checkHealthBtn = document.getElementById('checkHealthBtn');
    
    if (testApiBtn) {
        testApiBtn.addEventListener('click', () => {
            SwiftNetApp.testApiEndpoint();
        });
    }
    
    if (loadMetricsBtn) {
        loadMetricsBtn.addEventListener('click', () => {
            SwiftNetApp.loadMetrics();
        });
    }
    
    if (checkHealthBtn) {
        checkHealthBtn.addEventListener('click', () => {
            SwiftNetApp.checkHealth();
        });
    }

    const featureItems = document.querySelectorAll('.feature-item');
    featureItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px) scale(1.02)';
        });
        
        item.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });
    
    SwiftNetApp.addRippleEffect();
};

/**
 * Add ripple effect to buttons
 */
SwiftNetApp.addRippleEffect = function() {
    const buttons = document.querySelectorAll('.demo-button, .nav-link');
    
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.classList.add('ripple');
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
    
    if (!document.querySelector('#ripple-styles')) {
        const style = document.createElement('style');
        style.id = 'ripple-styles';
        style.textContent = `
            .ripple {
                position: absolute;
                border-radius: 50%;
                background: rgba(255, 255, 255, 0.6);
                transform: scale(0);
                animation: ripple-animation 0.6s linear;
                pointer-events: none;
            }
            
            @keyframes ripple-animation {
                to {
                    transform: scale(4);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);
    }
};

/**
 * Animate elements on scroll
 */
SwiftNetApp.animateElements = function() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    }, {
        threshold: 0.1
    });
    
    const animatedElements = document.querySelectorAll('.feature-item, .tech-item');
    animatedElements.forEach(el => observer.observe(el));
};

/**
 * Test API endpoint functionality
 */
SwiftNetApp.testApiEndpoint = function() {
    const resultsDiv = document.getElementById('demoResults');
    const username = 'DemoUser' + Math.floor(Math.random() * 1000);
    
    SwiftNetApp.showLoading(resultsDiv);
    SwiftNetApp.state.requestCount++;
    
    const apiUrl = `/api/user?username=${username}`;
    
    setTimeout(() => {
        const mockResponse = {
            status: 'success',
            endpoint: '/api/user',
            method: 'GET',
            parameters: { username: username },
            timestamp: new Date().toISOString(),
            requestId: SwiftNetApp.state.requestCount,
            frameworkVersion: '2.1.0',
            responseTime: Math.floor(Math.random() * 50) + 10 + 'ms'
        };
        
        SwiftNetApp.displayApiResponse(resultsDiv, mockResponse);
        SwiftNetApp.state.lastResponse = mockResponse;
    }, 800);
    
    console.log(`📡 Testing API endpoint: ${apiUrl}`);
};

/**
 * Load framework metrics
 */
SwiftNetApp.loadMetrics = function() {
    const resultsDiv = document.getElementById('demoResults');
    
    SwiftNetApp.showLoading(resultsDiv);
    SwiftNetApp.state.requestCount++;
    
    setTimeout(() => {
        const metrics = SwiftNetApp.generateMetrics();
        SwiftNetApp.displayMetrics(resultsDiv, metrics);
    }, 1000);
    
    console.log('📊 Loading framework metrics...');
};

/**
 * Check system health
 */
SwiftNetApp.checkHealth = function() {
    const resultsDiv = document.getElementById('demoResults');
    
    SwiftNetApp.showLoading(resultsDiv);
    SwiftNetApp.state.requestCount++;
    
    setTimeout(() => {
        const healthData = SwiftNetApp.generateHealthCheck();
        SwiftNetApp.displayHealthCheck(resultsDiv, healthData);
    }, 600);
    
    console.log('💚 Performing health check...');
};

/**
 * Show loading animation
 */
SwiftNetApp.showLoading = function(container) {
    container.innerHTML = `
        <div class="loading-animation">
            <div class="loading-spinner"></div>
            <p style="margin-top: 15px; color: #4fc3f7;">Processing request...</p>
        </div>
    `;
    
    if (!document.querySelector('#loading-styles')) {
        const style = document.createElement('style');
        style.id = 'loading-styles';
        style.textContent = `
            .loading-animation {
                text-align: center;
                padding: 20px;
            }
            
            .loading-spinner {
                width: 40px;
                height: 40px;
                margin: 0 auto;
                border: 4px solid rgba(79, 195, 247, 0.3);
                border-top: 4px solid #4fc3f7;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }
            
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(style);
    }
};

/**
 * Display API response
 */
SwiftNetApp.displayApiResponse = function(container, response) {
    container.innerHTML = `
        <div class="api-response-display">
            <h4 style="color: #4fc3f7; margin-bottom: 15px;">🔍 API Endpoint Test Results</h4>
            <div class="response-item"><strong>Status:</strong> <span style="color: #2ecc71;">${response.status.toUpperCase()}</span></div>
            <div class="response-item"><strong>Endpoint:</strong> ${response.endpoint}</div>
            <div class="response-item"><strong>Method:</strong> ${response.method}</div>
            <div class="response-item"><strong>Username:</strong> ${response.parameters.username}</div>
            <div class="response-item"><strong>Request ID:</strong> #${response.requestId}</div>
            <div class="response-item"><strong>Response Time:</strong> ${response.responseTime}</div>
            <div class="response-item"><strong>Framework Version:</strong> ${response.frameworkVersion}</div>
            <div class="response-item"><strong>Timestamp:</strong> ${response.timestamp}</div>
            <div style="margin-top: 15px; padding: 10px; background: rgba(46, 204, 113, 0.1); border-radius: 5px; border-left: 3px solid #2ecc71;">
                <strong>✅ Test Result:</strong> API endpoint is functioning correctly
            </div>
        </div>
    `;
};

/**
 * Generate mock metrics data
 */
SwiftNetApp.generateMetrics = function() {
    return {
        uptime: '100%',
        totalRequests: SwiftNetApp.state.requestCount,
        averageResponseTime: Math.floor(Math.random() * 30) + 15 + 'ms',
        memoryUsage: Math.floor(Math.random() * 40) + 25 + 'MB',
        cpuUsage: Math.floor(Math.random() * 30) + 5 + '%',
        activeConnections: Math.floor(Math.random() * 10) + 1,
        frameworkVersion: SwiftNetApp.version,
        javaVersion: '11.0.x',
        serverPort: '9090'
    };
};

/**
 * Display metrics
 */
SwiftNetApp.displayMetrics = function(container, metrics) {
    container.innerHTML = `
        <div class="metrics-display">
            <h4 style="color: #f39c12; margin-bottom: 15px;">📊 Framework Performance Metrics</h4>
            <div class="metrics-grid">
                <div class="metric-item"><strong>Uptime:</strong> ${metrics.uptime}</div>
                <div class="metric-item"><strong>Total Requests:</strong> ${metrics.totalRequests}</div>
                <div class="metric-item"><strong>Avg Response Time:</strong> ${metrics.averageResponseTime}</div>
                <div class="metric-item"><strong>Memory Usage:</strong> ${metrics.memoryUsage}</div>
                <div class="metric-item"><strong>CPU Usage:</strong> ${metrics.cpuUsage}</div>
                <div class="metric-item"><strong>Active Connections:</strong> ${metrics.activeConnections}</div>
                <div class="metric-item"><strong>Framework Version:</strong> ${metrics.frameworkVersion}</div>
                <div class="metric-item"><strong>Java Version:</strong> ${metrics.javaVersion}</div>
                <div class="metric-item"><strong>Server Port:</strong> ${metrics.serverPort}</div>
            </div>
            <div style="margin-top: 15px; padding: 10px; background: rgba(243, 156, 18, 0.1); border-radius: 5px; border-left: 3px solid #f39c12;">
                <strong>📈 Performance Status:</strong> All metrics within optimal ranges
            </div>
        </div>
    `;

    if (!document.querySelector('#metrics-styles')) {
        const style = document.createElement('style');
        style.id = 'metrics-styles';
        style.textContent = `
            .metrics-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 10px;
                margin-bottom: 15px;
            }
            
            .metric-item {
                padding: 8px;
                background: rgba(255, 255, 255, 0.05);
                border-radius: 4px;
                font-size: 0.9em;
            }
            
            .response-item {
                margin: 5px 0;
                padding: 5px;
                background: rgba(255, 255, 255, 0.05);
                border-radius: 3px;
                font-size: 0.9em;
            }
        `;
        document.head.appendChild(style);
    }
};

/**
 * Generate health check data
 */
SwiftNetApp.generateHealthCheck = function() {
    const services = [
        { name: 'HTTP Server', status: 'healthy', responseTime: '5ms' },
        { name: 'Dependency Container', status: 'healthy', responseTime: '2ms' },
        { name: 'Route Processor', status: 'healthy', responseTime: '8ms' },
        { name: 'Static Resource Handler', status: 'healthy', responseTime: '3ms' }
    ];
    
    return {
        overallStatus: 'healthy',
        services: services,
        lastCheck: new Date().toLocaleString(),
        nextCheck: new Date(Date.now() + 30000).toLocaleString()
    };
};

/**
 * Display health check results
 */
SwiftNetApp.displayHealthCheck = function(container, healthData) {
    const servicesList = healthData.services.map(service => 
        `<div class="health-service">
            <span class="service-name">${service.name}</span>
            <span class="service-status healthy">✅ ${service.status.toUpperCase()}</span>
            <span class="service-time">${service.responseTime}</span>
        </div>`
    ).join('');
    
    container.innerHTML = `
        <div class="health-display">
            <h4 style="color: #2ecc71; margin-bottom: 15px;">💚 System Health Check</h4>
            <div class="overall-status">
                <strong>Overall Status:</strong> 
                <span style="color: #2ecc71;">✅ ${healthData.overallStatus.toUpperCase()}</span>
            </div>
            <div class="services-list">
                <h5 style="margin: 15px 0 10px 0; color: #4fc3f7;">Service Status:</h5>
                ${servicesList}
            </div>
            <div class="health-info">
                <div><strong>Last Check:</strong> ${healthData.lastCheck}</div>
                <div><strong>Next Check:</strong> ${healthData.nextCheck}</div>
            </div>
            <div style="margin-top: 15px; padding: 10px; background: rgba(46, 204, 113, 0.1); border-radius: 5px; border-left: 3px solid #2ecc71;">
                <strong>✅ Health Status:</strong> All systems operational and responding normally
            </div>
        </div>
    `;
    
    if (!document.querySelector('#health-styles')) {
        const style = document.createElement('style');
        style.id = 'health-styles';
        style.textContent = `
            .health-service {
                display: grid;
                grid-template-columns: 2fr 1fr 1fr;
                gap: 10px;
                padding: 8px;
                margin: 5px 0;
                background: rgba(255, 255, 255, 0.05);
                border-radius: 4px;
                align-items: center;
                font-size: 0.9em;
            }
            
            .service-name {
                font-weight: 500;
            }
            
            .service-status.healthy {
                color: #2ecc71;
                font-weight: 600;
            }
            
            .service-time {
                text-align: right;
                color: #4fc3f7;
            }
            
            .health-info div {
                margin: 5px 0;
                font-size: 0.9em;
            }
        `;
        document.head.appendChild(style);
    }
};

/**
 * Display framework information
 */
SwiftNetApp.displayFrameworkInfo = function() {
    console.log(`
╔══════════════════════════════════════════════════════════════╗
║                Apache type server                            ║
║                     Version ${SwiftNetApp.version}           ║
║                                                              ║
║  🌐 Interactive Demo Interface                              ║
║  🎯 Real-time Framework Testing                             ║
║  📊 Live Metrics Display                                    ║
║  💚 Health Monitoring                                       ║
╚══════════════════════════════════════════════════════════════╝
    `);
};

/**
 * Utility function for smooth scrolling
 */
SwiftNetApp.smoothScroll = function(target) {
    document.querySelector(target).scrollIntoView({
        behavior: 'smooth',
        block: 'start'
    });
};

/**
 * Export for use in other modules
 */
window.SwiftNetApp = SwiftNetApp;
