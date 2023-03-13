package org.example.spring.reactive.web;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static org.example.spring.reactive.web.ApiController.DATABASE;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@EnableHypermediaSupport(type = HAL)
public class HypermediaController {

  // serves HTTP GET /hypermedia/employee/{key} methods
  // extracts links from a Spring WebFlux method.
  // returns Mono<EntityModel<Employee>>: wrapped Spring HATEOAS’s container for an object that includes links
  @GetMapping("/hypermedia/employees/{key}")
  Mono<EntityModel<Employee>> employee(@PathVariable String key) {
    Mono<Link> selfLink = linkTo( // extracts a link from a Spring WebFlux method invocation.
      methodOn(HypermediaController.class)  // performs a dummy invocation of a controller’s web method to gather information for building links.
              .employee(key)) //
          .withSelfRel() // labels selfLinkwith a selfhypermedia relation
          .toMono();

    Mono<Link> aggregateRoot = linkTo( //
      methodOn(HypermediaController.class) //
        .employees()) //
          .withRel(LinkRelation.of("employees"))// applies an arbitrary employeeshypermedia relation.
          .toMono(); // converts all link-building into a Mono<Link>.

    // combines two Monooperations and process the resultswhen both are complete.
    Mono<Tuple2<Link, Link>> links = Mono.zip(selfLink, aggregateRoot);
    // extract and bundle links with the fetched employee into a Spring HATEOAS EntityModelobject by mapping over Tuple2of the Mono<Link>object
    return links.map(objects -> EntityModel.of(DATABASE.get(key), objects.getT1(), objects.getT2()));
  }

  /*
   - Maps GET /hypermedia/employees to this method, the aggregate root.
   - selfLink points to this method, which is a fixed endpoint.
   - flattens selfLink and extracts every entry from DATABASE,
      leveraging the employee(String key) method to convert each entry into an EntityModel<Employee> with single-item links.
   - bundles everything into a Mono<List<EntityModel<Employee>>>
      before returning the converted Mono<CollectionModel<EntityModel <Employee>>> with the aggregate root’s selfLink wired in
   */
  @GetMapping("/hypermedia/employees")
  Mono<CollectionModel<EntityModel<Employee>>> employees() {
    Mono<Link> selfLink = linkTo( //
      methodOn(HypermediaController.class) //
        .employees()) //
          .withSelfRel() //
          .toMono();

    return selfLink //
      .flatMap(self -> Flux.fromIterable(DATABASE.keySet()) //
        .flatMap(key -> employee(key)) //
        .collectList()
        .map(entityModels -> CollectionModel.of(entityModels, self)));
  }
}
