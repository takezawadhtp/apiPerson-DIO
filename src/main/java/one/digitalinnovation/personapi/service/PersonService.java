package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO){
        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Created person with ID ");
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream().map(personMapper::toDTO).collect(Collectors.toList());
    }

    public PersonDTO findById(long id) throws PersonNotFoundException {
        Person person = verifyIfExistis(id);

        return personMapper.toDTO(person);
    }

    public void deleteById(long id) throws PersonNotFoundException {
        verifyIfExistis(id);
        personRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExistis(id);
        Person personToUpdate = personMapper.toModel(personDTO);

        Person updatedPerson = personRepository.save(personToUpdate);
        return createMessageResponse(updatedPerson.getId(), "Updated person with ID ");
    }

    private Person verifyIfExistis(long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}

