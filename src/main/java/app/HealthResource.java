package app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/healthcheck")
public class HealthResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthcheck() throws Exception {
        var result = Map.of(
            "endpoint-slide4vr-pdf2png",request("https://endpoint-slide4vr-pdf2png-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "pdf2png",request("https://pdf2png-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "endpoint-slide4vr-pptx2png",request("https://endpoint-slide4vr-pptx2png-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "pptx2png",request("https://pptx2png-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "slide4vr-on-transformed",request("https://slide4vr-on-transformed-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "endpoint-slide4vr-ssg",request("https://endpoint-slide4vr-ssg-dnb6froqha-uc.a.run.app/healthcheck").get().body(),
            "slide4vr-ssg",request("https://slide4vr-ssg-dnb6froqha-uc.a.run.app/healthcheck").get().body()
        );

        var json = new ObjectMapper().writer().writeValueAsString(result);
        return Response.ok(json).build();
    }

    private Future<HttpResponse<String>> request(String url) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}