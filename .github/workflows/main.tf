resource "aws_instance" "example" {
  ami           = "i-0cc5a298645472646" # desired AMI ID
  instance_type = var.instance_type

  tags = {
    Name = "ExampleInstance"
  }
}
