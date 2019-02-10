const express = require('express')
const PORT = 8080

const app = express()

app.post('/api/:version/door', async (req, res) => {
    const toReturn = {
        message: "The port is open"
    }

    res.send(toReturn)
})

app.post('/api/:version/gate', async (req, res) => {
    const toReturn = {
        message: "The gate is open"
    }

    res.send(toReturn)
})

app.listen(PORT, () => {
    console.log(`Server in ascolto sulla porta: ${PORT}`)
})