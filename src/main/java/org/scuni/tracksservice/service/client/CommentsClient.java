package org.scuni.tracksservice.service.client;

import org.scuni.tracksservice.dto.QueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient("comments-service")
public interface CommentsClient {
    @PostMapping(value = "api/v1/recommendations/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<Long>> getRecommendedCommentsIds(@RequestBody QueryDto query);
}
