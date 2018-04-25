package org.rest.dao;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.rest.model.Student;

import java.util.List;

/**
 * Student DAO in MongoDB database with Student class and Integer as primary Key
 */
public class MongoStudentDAO extends MongoGenericDAO<Student, Integer> {
    @Override
    public Student read(Integer index) {
        return datastore.createQuery(Student.class).field("index").equal(index).get();
    }

    @Override
    public boolean update(Student updateObject) {
        final Query<Student> studentToUpdate = datastore.createQuery(Student.class).field("index").equal(updateObject.getIndex());
        final UpdateOperations<Student> updateOperations = datastore.createUpdateOperations(Student.class)
                .set("firstName", updateObject.getFirstName())
                .set("lastName", updateObject.getLastName())
                .set("birthday", updateObject.getBirthday());
        datastore.update(studentToUpdate, updateOperations);
        return false;
    }

    @Override
    public List<Student> getAll() {
        final Query<Student> query = datastore.createQuery(Student.class);
        final List<Student> students = query.asList();
        return students;
    }
}
