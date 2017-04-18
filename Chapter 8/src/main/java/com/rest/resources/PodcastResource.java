package com.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import com.rest.domain.Podcast;
import com.rest.exception.NotFoundException;
import com.rest.service.PodcastService;

@Path("podcasts")
public class PodcastResource {

	@Autowired
	PodcastService podcastService;

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Podcast createPodcast(Podcast podcast) {

		podcastService.createPodcast(podcast);
		return podcast;
	}

//	@GET
//	@Path("{id}")
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Podcast getPodcast(@PathParam("id") int id) throws NotFoundException {
//		Podcast podcast = podcastService.getPodcast(id);
//		return podcast;
//	}

	@GET
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPodcast(@PathParam("id") int id) throws NotFoundException {
		Podcast podcast = podcastService.getPodcast(id);
		CacheControl cc = new CacheControl();
		cc.setMaxAge(864000);
		ResponseBuilder builder = Response.ok(podcast);
		builder.cacheControl(cc);
		return builder.build();
	}
	@GET
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getPodcast(@PathParam("id") int id,@Context Request request) throws NotFoundException {
		Podcast podcast = podcastService.getPodcast(id);
		CacheControl cc = new CacheControl();
		cc.setMaxAge(864000);
		EntityTag eTag = new EntityTag(Integer.toString(podcast.hashCode()));
		ResponseBuilder builder =  request.evaluatePreconditions(eTag);
		
		// podcast did change -> serve update content
		if(builder == null ){
			builder = Response.ok(podcast);
			builder.tag(eTag);
		}
		
		builder.cacheControl(cc);
		return builder.build();
	}

	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public List<Podcast> getPodcasts(@QueryParam("title") String title) {
		List<Podcast> podcasts = podcastService.getPodcasts(title);
		return podcasts;
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public void updatePodcast(@PathParam("id") int id, Podcast update) {
		podcastService.updatePodcast(id, update);
	}

	@DELETE
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public void deletePodcast(@PathParam("id") int id) {
		podcastService.deletePodcast(id);
	}
}
