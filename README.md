The Advanced Scientific & Graphing Calculator is a comprehensive mathematical computation tool designed to evaluate complex mathematical expressions and generate graphical representations in both two-dimensional (2D) and three-dimensional (3D) spaces.

This project aims to provide a precise, efficient, and user-friendly environment for performing advanced mathematical operations, including higher-level functions and visual analysis through graphical plotting.

Features
1. Expression Evaluation
Arithmetic operations (addition, subtraction, multiplication, division, modulus)
Exponentiation and roots
Trigonometric functions (sin, cos, tan, etc.)
Inverse trigonometric functions
Logarithmic and exponential functions
Factorials and combinatorics
Absolute value and advanced algebraic expressions
Support for nested expressions
2. Complex Function Handling
Evaluation of multi-operator expressions
Support for function chaining
Implicit mathematical constants (π, e)
Structured parsing for accurate precedence handling
3. 2D Graphing
Plotting single-variable functions (e.g., y = f(x))
Adjustable domain ranges
Zooming and scaling capabilities
Coordinate grid rendering
Axis labeling and real-time graph updates
4. 3D Graphing
Plotting surface functions (e.g., z = f(x, y))
Rotatable 3D visualization
Adjustable viewing angles
Surface mesh rendering
Dynamic scaling and navigation controls
Technical Architecture

The calculator is structured with modular components to ensure clarity, maintainability, and scalability:

Expression Parser Module
Responsible for tokenization, precedence handling, and mathematical evaluation.
Computation Engine
Executes parsed mathematical instructions and returns numerical results.
2D Rendering Engine
Handles graph plotting in Cartesian coordinates.
3D Rendering Engine
Generates and displays three-dimensional surfaces using coordinate transformations.
User Interface Layer
Manages user inputs, displays outputs, and coordinates interaction between modules.
Mathematical Capabilities

The system follows standard mathematical conventions including:

BODMAS/PEMDAS precedence rules
Support for nested parentheses
Accurate floating-point calculations
Domain validation for functions
Error handling for undefined expressions
