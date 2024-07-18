const { createMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        "ws",
        createMiddleware('/', {
            target: 'http://localhost:30001',
            changeOrigin: true,
            ws: true
        })
    )
}