resource "aws_instance" "example" {
  ami           = "ami-0c55b159cbfafe1f0" # Replace with your desired AMI ID
  instance_type = var.instance_type

  tags = {
    Name = "ExampleInstance"
  }
}
