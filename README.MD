## Validation

The requirements do not specify a particular implementation for validation, so I have split it into two stages. 
One part involves checking that required fields are not null, and the other involves verifying business logic 
(e.g., whether a user exists). I decided to leave field validation for the next module, where data will arrive 
via controllers and can be validated like this:

```java
@RestController
@RequestMapping("/publications")
public class PublicationController {

    @PostMapping
    public ResponseEntity<@NonNull UUID> createPublication(
        @Valid @RequestBody CreatePublicationDto dto
    ) {
        var publication = conversionService.convert(dto, Publication.class);
        var uid = publicationService.create(publication);
        return new ResponseEntity<>(uid, HttpStatus.CREATED);
    }
}

public record CreatePublicationDto(

    @NotNull(message = "Publication date must not be null")
    @PastOrPresent(message = "Publication date must be in the past or present")
    LocalDate publicationDate,

    @Min(value = 1, message = "Page count must be at least 1")
    @Positive(message = "Page count must be positive")
    int pageCount,

    @NotNull(message = "Price must not be null")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 19, fraction = 2, message = "Price must have up to 19 integer digits and 2 fractional digits")
    BigDecimal price,

    @NotNull(message = "Publisher UID must not be null")
    UUID publisherUid,

    @NotNull(message = "Book UID must not be null")
    UUID bookUid
) {
}
```

On the other hand, checking if entities exist, ensuring they are active, and verifying availability is handled within 
the service methods. I decided against using separate validator classes because, in many implementations, validators 
fetch all necessary data from the repository, and then the service itself fetches the same data again if validation
passes. This results in redundant repository calls.
I also considered creating a custom "context" class to load data incrementally and validate it step-by-step before 
passing it to the service. However, this approach would significantly overcomplicate the application and make it harder 
to understand how specific service methods work (KISS).
Now I have such validations:
### Update Trainee
- check birthdate
- check trainee exist
### Create Trainer
- check specialization exist
### Update Trainer
- check trainer exist
### Change password
- check entity exist and old password are valid
### Assign trainee to trainer
- check trainee exist
- check trainer exist
- check trainee not assigned to trainer before
### Get trainers assigned to trainee
- check trainee exist
### Get trainers unassigned to trainee
- check trainee exist
### Create training
- check trainee exist
- check trainer exist
- check trainer assigned to trainee
- check training type exist
- check type of training correspond trainer specialization
- check training date is valid (not in the past, no further than 1 year)
- check trainee free on date
- check trainer free on date
### Get trainee/trainer trainings
- check trainee/trainer exist
- check training type exist

## Facade

The requirements are also silent regarding the specific purpose of the Facade. For now, I have limited its scope to 
simple delegation with logging for a single service method. If it is needed later as a unified entry point for the 
service layer, I will expand it. However, until controllers and authentication/authorization are implemented, its 
utility is minimal.

## Auth

The task states that access to methods excluding the creation of a trainee or trainer must be authorized. It isn't 
entirely clear what this entails—whether it means passing the username and password with every DTO for verification. 
I assume that in future modules, we will be asked to implement Spring Security, where this will be handled via tokens
or sessions.
Furthermore, it isn't specified who is authorized to create trainings. In typical applications, this is handled by 
administrators who manage requests. Currently, based on the requirements, it appears the trainers or trainees 
themselves manage this.
