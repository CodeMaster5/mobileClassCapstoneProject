//
//  LoginViewController.swift
//  BarkOrBolt-iOS
//
//  Created by Khuram Chaudhry on 4/11/17.
//  Copyright © 2017 Silicon Empire LLC. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {
    
    @IBOutlet weak var createAccountLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        let tap = UITapGestureRecognizer(target: self, action: #selector(LoginViewController.tapFunction))
        createAccountLabel.isUserInteractionEnabled = true
        createAccountLabel.addGestureRecognizer(tap)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tapFunction(sender:UITapGestureRecognizer) {
        let createAccountViewControllerObj = self.storyboard?.instantiateViewController(withIdentifier:"CreateAccount") as? CreateAccountViewController
        self.navigationController?.pushViewController(createAccountViewControllerObj!, animated: true)    }
    
}
