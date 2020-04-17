import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._

import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._


object Main extends App {
  var window: Long = NULL
  run()

  private def run() = {
    init()
    loop()

    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate
    glfwSetErrorCallback(null).free

  }

  private def init() = {
    GLFWErrorCallback.createPrint(System.err).set

    if (!(glfwInit)) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
    if (window == null) {
      throw new RuntimeException("Failed to create the GLFW window")
    }

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)


  }
  private def loop() = {
    import org.lwjgl.opengl.GL
    GL.createCapabilities

    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
    while ( {
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      glfwSwapBuffers(window)
      glfwPollEvents
    }

  }
}
