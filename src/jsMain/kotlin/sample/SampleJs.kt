package sample

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document

@JsName("setupUI")
fun setupUI() {
    val button = document.getElementById("step-simulation") as HTMLButtonElement
    button.onclick = {
        simulationStep()
    }
    simulationStep()
}

var step = 0
var world = gliderWorld

fun simulationStep() {
    val image = document.getElementById("conway-image") as HTMLImageElement
    image.src = "/image/$step"
    step++
    render(world)
    world = world.map(World::conwayRules)
}

fun render(w: World) {
    val canvas = document.getElementById("conway-canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.clearRect(0.0, 0.0, canvas.width.toDouble() * 10, canvas.height.toDouble() * 10)
    context.beginPath()
    w.forEachAlive { x, y ->
        context.rect(x * 10.0, y * 10.0, 10.0, 10.0)
    }
    context.fill()
}