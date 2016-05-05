/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import io.techplex.borderblocks.plumbing.Main;
import java.util.concurrent.ConcurrentLinkedQueue;
import sinetja.Server;
/**
 *
 * @author techplex
 */
public class WebApi {
	private static WebApi inst = null;
	Server apiserver;
	
	ConcurrentLinkedQueue<ApiAction> queue = new ConcurrentLinkedQueue<>();

	
	public static WebApi getInstance() {
		if (inst == null) {
			inst = new WebApi();
		}
		return inst;
	}
	
	public ApiAction getNextApiAction() {
		return queue.poll();
	}
	
	/**
	 * Start the HTTP server thread
	 */
	public void start() {
		if (apiserver == null) {
			apiserver = new Server();
			apiserver.GET("/", (req, res) -> {
				res.respondText("Hello world");
			})
			.GET("/hello/:name", (req, res) -> {
				String name = req.param("name");
				queue.add(new ApiAction(name));
				res.respondText("Hello " + name);
			})
			.start(8000);
			
		}
		Main.getInstance().getLogger().warning("apiserver already started");
	}
	public void stop() {
		Main.getInstance().getLogger().warning("At this time, it is not possible to stop the WebAPI server. ");
	}
	
}
