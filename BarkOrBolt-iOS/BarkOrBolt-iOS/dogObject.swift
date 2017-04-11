//
//  dogObject.swift
//  BarkOrBolt-iOS
//
//  Created by Khuram Chaudhry on 4/11/17.
//  Copyright © 2017 Silicon Empire LLC. All rights reserved.
//  Data Model for the dogUser.

import UIKit

class dogObject {
    
    var dogImage: UIImage?
    var name: String
    var email: String
    var dateJoined: Date
    var breed: String
    var gender: String
    var ownerName: String
    
    init(dogImage: UIImage?, name: String, email: String, dateJoined: Date, breed: String, gender: String,
         ownerName: String) {
        self.dogImage = dogImage
        self.name = name
        self.email = email
        self.dateJoined = dateJoined
        self.breed = breed
        self.gender = gender
        self.ownerName = ownerName
    }
    
    func getDogImage() -> UIImage {
        return self.dogImage!
    }
    
    func getName() -> String {
        return self.name
    }
    
    func getEmail() -> String {
        return self.email
    }
    
    func getDateJoined() -> Date {
        return self.dateJoined
    }
    
    func getBreed() -> String {
        return self.breed
    }
    
    func getGender() -> String {
        return self.gender
    }
    
    func getOwnerName() -> String {
        return self.ownerName
    }
    
}
