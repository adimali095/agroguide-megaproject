package com.example.recycler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelperClass
{
    private final FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private static final String userNodePath = "users";

    DatabaseHelperClass()
    {
        rootNode = FirebaseDatabase.getInstance();
    }
    public void addUser(UserHelperClass user)
    {
        reference = rootNode.getReference(userNodePath);
        reference.child(user.getUsername()).setValue(user);
    }

}
