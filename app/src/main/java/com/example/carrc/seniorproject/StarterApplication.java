/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.example.carrc.seniorproject;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())


            .applicationId("f2fd6a3b52b4f53d0cd5fcd8c0f48bebc3720ea9")
            .clientKey("d34f89bd804abf8f1f5f7c71319733ef4f4297cc")
            .server("http://ec2-35-161-116-134.us-west-2.compute.amazonaws.com:80/parse")
            /*
            .applicationId("f483e6fd1d58679515bacc93cd11b2b909976cee")
            .clientKey("4de5d6fc6087633ad897ac7c0db3abb4b9ce8fd9")
            .server("http://ec2-52-36-220-73.us-west-2.compute.amazonaws.com:80/parse")
            */
            .build()
    );

      // to access parse server GUI, go to:
      // http://ec2-52-36-220-73.us-west-2.compute.amazonaws.com:80/apps
      // username: user
      // password: qKcWnh9GJiNl
      // all users in the database passwords are = pass


    ParseUser.enableAutomaticUser();

    // if first time running the app, set up the database entries and roles

    // Query the parse server for the roles that are saved
    ParseQuery<ParseRole> parseRoleParseQuery = ParseRole.getQuery();

      try {
          List<ParseRole> objects = parseRoleParseQuery.find();
          Log.i("First Run", String.valueOf(objects.size()));

          if(objects.isEmpty()){

              // make sure no users are signed into the app by any means
              ParseUser user = new ParseUser();
              user.getCurrentUser().logOut();

              // create an ACL that has read only access
              ParseACL readOnlyRoleACL = new ParseACL();
              readOnlyRoleACL.setPublicReadAccess(true);

              // create an ACL that has read and write access
              ParseACL fullAccessRoleACL = new ParseACL();
              fullAccessRoleACL.setPublicReadAccess(true);
              fullAccessRoleACL.setPublicWriteAccess(true);

              // create the manager, customer, and employee roles
              ParseRole managerRole = new ParseRole("Manager", fullAccessRoleACL);
              ParseRole customerRole = new ParseRole("Customer", readOnlyRoleACL);
              ParseRole employeeRole = new ParseRole("Employee", readOnlyRoleACL);

              // save each role to the database
              managerRole.saveInBackground();
              customerRole.saveInBackground();
              employeeRole.saveInBackground();

              // create the default manager account and save to database
              ParseUser managerUser = new ParseUser();
              managerUser.setUsername("manager");
              managerUser.setPassword("pass");
              managerUser.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {
                      if(e == null){
                          Log.i("Manager Account", "Success");
                      } else {
                          Log.i("Manager Account", "Failed " + e.toString());
                      }
                  }
              });

              SystemClock.sleep(5000);

              // add the default manager account to the manager role
              managerRole.getUsers().add(managerUser);
              managerRole.saveInBackground(new SaveCallback() {
                  @Override
                  public void done(ParseException e) {
                      if(e == null){
                          Log.i("Manager Role", "Success");
                      } else {
                          Log.i("Manager Role", "Failed " + e.toString());
                      }
                  }
              });

              // log out of the default manager account
              user.getCurrentUser().logOut();

          }


      } catch (ParseException e) {
          e.printStackTrace();
      }

        /*
      parseRoleParseQuery.findInBackground(new FindCallback<ParseRole>() {
      @Override
      public void done(List<ParseRole> objects, ParseException e) {
        // if no roles exist, this is the first time the app has been run
        if(objects == null){

          // make sure no users are signed into the app by any means
          ParseUser user = new ParseUser();
          user.getCurrentUser().logOut();

          // create an ACL that has read only access
          ParseACL readOnlyRoleACL = new ParseACL();
          readOnlyRoleACL.setPublicReadAccess(true);

          // create an ACL that has read and write access
          ParseACL fullAccessRoleACL = new ParseACL();
          fullAccessRoleACL.setPublicReadAccess(true);
          fullAccessRoleACL.setPublicWriteAccess(true);

          // create the manager, customer, and employee roles
          ParseRole managerRole = new ParseRole("Manager", fullAccessRoleACL);
          ParseRole customerRole = new ParseRole("Customer", readOnlyRoleACL);
          ParseRole employeeRole = new ParseRole("Employee", readOnlyRoleACL);

          // save each role to the database
          managerRole.saveInBackground();
          customerRole.saveInBackground();
          employeeRole.saveInBackground();

          // create the default manager account and save to database
          ParseUser managerUser = new ParseUser();
          managerUser.setUsername("manager");
          managerUser.setPassword("pass");
          managerUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
              if(e == null){
                Log.i("Manager Account", "Success");
              } else {
                Log.i("Manager Account", "Failed " + e.toString());
              }
            }
          });

          SystemClock.sleep(5000);

          // add the default manager account to the manager role
          managerRole.getUsers().add(managerUser);
          managerRole.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
              if(e == null){
                Log.i("Manager Role", "Success");
              } else {
                Log.i("Manager Role", "Failed " + e.toString());
              }
            }
          });

          // log out of the default manager account
          user.getCurrentUser().logOut();

        }
      }
    });
    */


    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
