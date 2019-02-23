const express = require('express')
const PORT = 8080

const app = express()

app.get('/api/:version/door', async (req, res) => {
    const message = "The port is open"
    const toReturn = {
        message
    }

    console.log(message)
    res.send(toReturn)
})

app.get('/api/:version/gate', async (req, res) => {
    const message = "The gate is open"
    const toReturn = {
        message
    }

    console.log(message)
    res.send(toReturn)
})

app.listen(PORT, () => {
    console.log(`Server in ascolto sulla porta: ${PORT}`)
})