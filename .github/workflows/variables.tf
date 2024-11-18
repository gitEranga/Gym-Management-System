variable "aws_region" {
  description = "The AWS region to deploy resources"
  type        = string
  default     = "ap-northeast-2"
}

variable "instance_type" {
  description = "Type of EC2 instance"
  type        = string
  default     = "t2.nano"
}
