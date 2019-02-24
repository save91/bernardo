const express = require('express')
const sleep = require('./utils/sleep')
const PORT = 8080
const SLEEP_TIME = 3000

const app = express()

app.use(function (req, res, next) {
    console.log('Time:', Date.now());
    console.log('Request Type:', req.method);
    console.log('Request URL:', req.originalUrl);

    next();
});

app.get('/api/:version/door', async (req, res) => {
    const message = "The port is open"
    const toReturn = {
        message
    }

    await sleep(SLEEP_TIME);
    console.log(message)
    res.send(toReturn)
})

app.get('/api/:version/gate', async (req, res) => {
    const message = "The gate is open"
    const toReturn = {
        message
    }

    await sleep(SLEEP_TIME);
    console.log(message)
    res.send(toReturn)
})

app.listen(PORT, () => {
    console.log(`Server in ascolto sulla porta: ${PORT}`)
})