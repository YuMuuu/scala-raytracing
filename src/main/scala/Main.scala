//https://www.lwjgl.org/guide

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._
import java.nio._

import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

import scala.util.Try


object HelloWorld extends App {
  new HelloWorld().run()
}

class HelloWorld {
  def run(): Unit = {
    println("Hello LWJGL " + Version.getVersion + "!")
    for {
      window <- init()
    } yield {
      loop(window)
      glfwFreeCallbacks(window)
      glfwDestroyWindow(window)
    }

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def init(): Option[Long] = {
    GLFWErrorCallback.createPrint(System.err).set
    Option(glfwInit()) match {
      case None => Left(throw new IllegalStateException("Unable to initialize GLFW"))
      case _ => {
        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
      }
    }

    for {
      window: Long <- Option(glfwCreateWindow(300, 300, "Hello World!", NULL, NULL))
    } yield {
      // Setup a key callback. It will be called every time a key is pressed, repeated or released.
      glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
      })

      // Get the thread stack and push a new frame
      Try {
        val pWidth = stackPush.mallocInt(1) // int*
        val pHeight = stackPush.mallocInt(1)
        // Get the window size passed to glfwCreateWindow
        glfwGetWindowSize(window, pWidth, pHeight)
        // Get the resolution of the primary monitor
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
        // Center the window
        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)

        // the stack frame is popped automatically
        stackPush.close()
      }

      // Make the OpenGL context current
      glfwMakeContextCurrent(window)
      // Enable v-sync
      glfwSwapInterval(1)
      // Make the window visible
      glfwShowWindow(window)

      window
    }
  }

  private def loop(window: Long): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer
      glfwSwapBuffers(window) // swap the color buffers
      glfwPollEvents()
    }
  }
}