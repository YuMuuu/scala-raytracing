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
      case None => {
        throw new IllegalStateException("Unable to initialize GLFW")
        System.exit(-1)
      }
      case _ => {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
      }
    }

    for {
      window: Long <- Option(glfwCreateWindow(300, 300, "Hello World!", NULL, NULL))
    } yield {
      glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
      })

      Try {
        val pWidth = stackPush.mallocInt(1)
        val pHeight = stackPush.mallocInt(1)
        glfwGetWindowSize(window, pWidth, pHeight)
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)

        stackPush.close()
      }

      glfwMakeContextCurrent(window)
      glfwSwapInterval(1)
      glfwShowWindow(window)

      window
    }
  }

  private def loop(window: Long): Unit = {
    GL.createCapabilities
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }
}