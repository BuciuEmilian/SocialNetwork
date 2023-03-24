package com.example.socialnetwork.repository.file;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        this.loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E e = this.stringToEntity(attributes);
                super.save(e);
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * saves the given entity into the file
     *
     * @param entity - the entity to be added
     */
    private void writeToFile(E entity) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(entityToString(entity));
            bw.newLine();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *  builds and entity from the list of strings given as argument
     *
     * @param attributes - the attributes of the entity to be constructed
     * @return the constructed entity
     */
    public abstract E stringToEntity(List<String> attributes);

    /**
     *  builds a string from the entity given as argument
     *
     * @param e - the entity to be converted
     * @return - the string representing the attributes of the entity separated by ;
     */
    public abstract String entityToString(E e);

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        if (e == null)
            this.writeToFile(entity);

        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        this.findAll().forEach(this::writeToFile);

        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        this.findAll().forEach(this::writeToFile);

        return e;
    }
}
