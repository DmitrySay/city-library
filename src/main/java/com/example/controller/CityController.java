package com.example.controller;

import com.example.dto.CityDto;
import com.example.service.CityService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.security.SecurityPermission.AUTHORITY_ALL;
import static com.example.security.SecurityPermission.AUTHORITY_CREATE;
import static com.example.security.SecurityPermission.AUTHORITY_UPDATE;

@RestController
@RequestMapping("api/cities")
@RequiredArgsConstructor
@Tag(name = "City")
public class CityController {
    private final CityService cityService;

    @Operation(summary = "Get cities", description = "Get pageable list of cities",
            parameters = {
                    @Parameter(
                            name = "page",
                            schema = @Schema(type = "integer", minimum = "0"),
                            in = ParameterIn.QUERY,
                            description = "Zero based page number of the cities you want to retrieve"),
                    @Parameter(
                            name = "size",
                            schema = @Schema(type = "integer", minimum = "1"),
                            in = ParameterIn.QUERY,
                            description = "The amount of cities you want to retrieve"),
                    @Parameter(
                            name = "sort",
                            schema = @Schema(type = "string", allowableValues = {"id", "name, ", "photo"}, defaultValue = "id"),
                            in = ParameterIn.QUERY,
                            description = "The field by which can be sorted." +
                                    "Default sort order is ascending. " +
                                    "Sorting criteria in the format: property(,asc|desc). "),
                    @Parameter(
                            name = "search",
                            schema = @Schema(type = "string"),
                            in = ParameterIn.QUERY,
                            description = "Search by name."

                    )
            })
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDto.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CityDto> getAll(@RequestParam(required = false) String search,
                                @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        return cityService.getAll(search, pageable);
    }

    @Operation(summary = "Update city", description = "Update city")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDto.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    @PreAuthorize("hasAuthority('" + AUTHORITY_ALL + "') or hasAuthority('" + AUTHORITY_UPDATE + "')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "{cityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CityDto update(@PathVariable("cityId") Long cityId, @Valid @RequestBody CityDto cityDto) {
        return cityService.update(cityId, cityDto);
    }

    @Hidden
    @PreAuthorize("hasAuthority('" + AUTHORITY_ALL + "') or hasAuthority('" + AUTHORITY_CREATE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CityDto create(@Valid @RequestBody CityDto cityDto) {
        return cityService.create(cityDto);
    }
}
