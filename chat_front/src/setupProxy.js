const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        ['ws','/api'],
        createProxyMiddleware({
            target: 'http://localhost:30001',
            changeOrigin: true,
            ws: true
        })
    )
}