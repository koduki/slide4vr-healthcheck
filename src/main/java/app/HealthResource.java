package app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/healthcheck")
public class HealthResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response healthcheck() throws Exception {
        var requests = Map.of(
            "endpoint-slide4vr-pdf2png",request("https://endpoint-slide4vr-pdf2png-dnb6froqha-uc.a.run.app/healthcheck"), 
            "pdf2png", request("https://pdf2png-dnb6froqha-uc.a.run.app/healthcheck"), 
            "endpoint-slide4vr-pptx2png", request("https://endpoint-slide4vr-pptx2png-dnb6froqha-uc.a.run.app/healthcheck"), 
            "pptx2png",request("https://pptx2png-dnb6froqha-uc.a.run.app/healthcheck"), 
            "slide4vr-on-transformed", request("https://slide4vr-on-transformed-dnb6froqha-uc.a.run.app/healthcheck"), 
            "endpoint-slide4vr-ssg", request("https://endpoint-slide4vr-ssg-dnb6froqha-uc.a.run.app/healthcheck"), 
            "slide4vr-ssg", request("https://slide4vr-ssg-dnb6froqha-uc.a.run.app/healthcheck")
        );

        var result = requests.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> {
            try {
                return x.getValue().get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }));

        var json = new ObjectMapper().writer().writeValueAsString(result);
        return Response.ok(json).build();
    }

    private Future<Map<String, Integer>> request(String url) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        var start = System.currentTimeMillis();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(r -> CompletableFuture.supplyAsync(() -> (Map.of("status", r.statusCode(), "response(ms)",
                        (int) (System.currentTimeMillis() - start)))));
    }
}