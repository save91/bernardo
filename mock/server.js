const express = require('express')
const bodyParser = require('body-parser')
const sleep = require('./utils/sleep')
const env = require('./environments/env')
const PORT = 8080
const SLEEP_TIME = 3000

const app = express()

app.use(bodyParser.urlencoded({ extended: true}))

app.use(function (req, res, next) {
    console.log('Time:', Date.now())
    console.log('Request Type:', req.method)
    console.log('Request URL:', req.originalUrl)
    console.log('Request body: ', req.body)

    const body = req.body || {}
    const secret = env.secret || {}
    if (body.cs === secret) {
        next()
    } else {
        res.status(403).send("Sorry! You can't see that.")
    }
});

app.post(env.path, async (req, res) => {
    const body = req.body || {}
    const ids = []
    ids.push(env.idDoor)
    ids.push(env.idGate)

    if (!ids.includes(body.id)) {
        res.status(404).send("Sorry! I don't know what is that")
        return
    }

    let message = "The port is open"
    switch(body.id) {
        case env.idDoor:
            message = "The port is open"
            break
        case env.idGate:
            message = "The gate is open"
            break
    }

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