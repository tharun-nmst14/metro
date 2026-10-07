package com.metro.ufm

import com.metro.ufm.config.ApplicationSettings
import java.util.EnumSet
import javax.servlet.DispatcherType
import org.apache.wicket.protocol.http.WicketFilter
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler

object MetroUfmServer {
  def main(args: Array[String]): Unit = {
    val settings = ApplicationSettings.load()
    val server = new Server(settings.port)
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)

    context.setContextPath("/")
    context.addServlet(classOf[DefaultServlet], "/")

    val wicketFilter = new FilterHolder(classOf[WicketFilter])
    wicketFilter.setInitParameter("applicationClassName", classOf[MetroUfmApplication].getName)
    wicketFilter.setInitParameter("filterMappingUrlPattern", "/*")
    context.addFilter(wicketFilter, "/*", EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ERROR))

    server.setHandler(context)
    server.start()
    println(s"Metro UFM Demo foundation is running at http://localhost:${settings.port}/")
    server.join()
  }
}
