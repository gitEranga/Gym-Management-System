resource "aws_instance" "example" {
  ami           = "ami-040c33c6a51fd5d96" # desired AMI ID
  instance_type = var.instance_type

  tags = {
    Name = "ExampleInstance"
  }
}
