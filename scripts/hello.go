package main

import (
	"fmt"
	"os"
)

func main() {
	fmt.Println("Hello, 世界")
	fmt.Fprintf(os.Stderr, "Ganesan = %s\n", "hello")
}
