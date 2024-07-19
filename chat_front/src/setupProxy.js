const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        createProxyMiddleware('ws', {
            target: 'http://localhost:30001',
            changeOrigin: true,
            ws: true
        })
    )
}