const express = require('express')
const sleep = require('./utils/sleep')
const PORT = 8080

const app = express()

app.get('/api/:version/door', async (req, res) => {
    const message = "The port is open"
    const toReturn = {
        message
    }

    await sleep(3000);
    console.log(message)
    res.send(toReturn)
})

app.get('/api/:version/gate', async (req, res) => {
    const message = "The gate is open"
    const toReturn = {
        message
    }

    await sleep(3000);
    console.log(message)
    res.send(toReturn)
})

app.listen(PORT, () => {
    console.log(`Server in ascolto sulla porta: ${PORT}`)
})