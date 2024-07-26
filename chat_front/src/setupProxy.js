const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        ['/api'],
        createProxyMiddleware({
            target: 'http://localhost:30001',
            changeOrigin: true
        })
    ),
    app.use(
        ['ws'],
        createProxyMiddleware({
            target: 'http://localhost:30001',
            changeOrigin: true,
            ws: true
        })
    )
}