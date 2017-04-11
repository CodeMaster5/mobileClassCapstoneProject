//
//  CreateAccountViewController.swift
//  BarkOrBolt-iOS
//
//  Created by Khuram Chaudhry on 4/11/17.
//  Copyright © 2017 Silicon Empire LLC. All rights reserved.
//

import UIKit

class CreateAccountViewController: UIViewController {
    
    @IBOutlet weak var loginLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        let tap = UITapGestureRecognizer(target: self, action: #selector(CreateAccountViewController.tapFunction))
        loginLabel.isUserInteractionEnabled = true
        loginLabel.addGestureRecognizer(tap)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tapFunction(sender:UITapGestureRecognizer) {
        navigationController?.popViewController(animated: true)
    }
    
}
