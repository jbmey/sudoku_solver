# Sudoku Solver Web Application

A modern, responsive web application for solving Sudoku puzzles built with Spring Boot and vanilla JavaScript. The application provides an intuitive interface for inputting Sudoku grids and automatically generates solutions using a backtracking algorithm.

## 🌐 Live Demo

The application is deployed on Render and accessible at:
**https://sudoku-solver-ef1r.onrender.com**

> Note: Replace `[your-service-hash]` with your actual Render service URL once deployed.

## ✨ Features

- **Interactive Sudoku Grid**: 9x9 grid with input validation
- **Automatic Solver**: Advanced backtracking algorithm for puzzle solving
- **Multiple Solutions**: Support for puzzles with multiple valid solutions
- **Responsive Design**: Mobile-friendly interface that works on all devices
- **Modern UI**: Clean, gradient-based design with glassmorphism effects
- **Real-time Validation**: Instant feedback on grid validity
- **Console Logging**: Detailed solution logging for debugging
- **RESTful API**: Backend API for programmatic access

## 🛠 Technology Stack

### Backend
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.4.0** - Web framework and dependency injection
- **Spring Web** - RESTful web services
- **Spring Boot Actuator** - Health checks and monitoring
- **Maven 3.9.4** - Dependency management and build tool

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Modern styling with Grid Layout and Flexbox
- **Vanilla JavaScript** - Clean, dependency-free frontend logic
- **Responsive Design** - Mobile-first approach

### DevOps & Deployment
- **Docker** - Containerization with multi-stage builds
- **GitHub Actions** - CI/CD pipeline with automated testing
- **Render** - Cloud hosting platform
- **Maven Wrapper** - Consistent build environment

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Docker (for containerized deployment)

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/jbmey/sudoku_solver.git
   cd sudoku_solver
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Access the application**
   - Open your browser to `http://localhost:8080`
   - The application will load with an empty Sudoku grid

### Building for Production

1. **Create JAR file**
   ```bash
   ./mvnw clean package
   ```

2. **Run the JAR**
   ```bash
   java -jar target/sudoku_solver-0.0.1.jar
   ```

## 🐳 Docker Deployment

### Build and Run Locally

```bash
# Build the Docker image
docker build -t sudoku-solver .

# Run the container
docker run -p 8080:8080 sudoku-solver
```

### Using Docker Compose (optional)

```yaml
version: '3.8'
services:
  sudoku-solver:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
```

## 📡 API Documentation

### Solve Sudoku Puzzle

**POST** `/api/sudoku/solve`

**Request Body:**
```json
[
  [5,3,0,0,7,0,0,0,0],
  [6,0,0,1,9,5,0,0,0],
  [0,9,8,0,0,0,0,6,0],
  [8,0,0,0,6,0,0,0,3],
  [4,0,0,8,0,3,0,0,1],
  [7,0,0,0,2,0,0,0,6],
  [0,6,0,0,0,0,2,8,0],
  [0,0,0,4,1,9,0,0,5],
  [0,0,0,0,8,0,0,7,9]
]
```

**Response:**
```json
{
  "solution1": [
    [5,3,4,6,7,8,9,1,2],
    [6,7,2,1,9,5,3,4,8],
    [1,9,8,3,4,2,5,6,7],
    [8,5,9,7,6,1,4,2,3],
    [4,2,6,8,5,3,7,9,1],
    [7,1,3,9,2,4,8,5,6],
    [9,6,1,5,3,7,2,8,4],
    [2,8,7,4,1,9,6,3,5],
    [3,4,5,2,8,6,1,7,9]
  ],
  "solution2": [
    [5,3,4,6,7,8,9,1,2],
    [6,7,2,1,9,5,3,4,8],
    [1,9,8,3,4,2,5,6,7],
    [8,5,9,7,6,1,4,2,3],
    [4,2,6,8,5,3,7,9,1],
    [7,1,3,9,2,4,8,5,6],
    [9,6,1,5,3,7,2,8,4],
    [2,8,7,4,1,9,6,3,5],
    [3,4,5,2,8,6,1,7,9]
  ],
  "multiplesSolutions": false,
  "noSolution": false
}
```

**Response Fields:**
- `solution1`: 9x9 array containing the first solution (using ascending order for backtracking)
- `solution2`: 9x9 array containing the second solution (using descending order for backtracking)
- `multiplesSolutions`: Boolean indicating if the puzzle has multiple valid solutions
- `noSolution`: Boolean indicating if the puzzle has no valid solution (both solution1 and solution2 will be null)

**Note:** The API attempts to find solutions using two different approaches:
- Solution 1: Backtracking with numbers 1-9 in ascending order
- Solution 2: Backtracking with numbers 9-1 in descending order

If both solutions are identical, `multiplesSolutions` will be `false`. If no solution exists, both solution arrays will be `null` and `noSolution` will be `true`.

### Health Check

**GET** `/actuator/health`

Returns application health status for monitoring.

## 🧪 Testing

### Run Unit Tests
```bash
./mvnw test
```

### Run Integration Tests
```bash
./mvnw verify
```

### Test Coverage
```bash
./mvnw test jacoco:report
```

## 📱 Mobile Support

The application is fully responsive and includes:

- **Adaptive Grid Sizing**: Grid cells automatically resize for different screen sizes
- **Touch-Friendly Interface**: Optimized button sizes for mobile interaction
- **Responsive Typography**: Scalable fonts for readability across devices
- **Viewport Optimization**: Proper scaling without horizontal scrolling

### Supported Breakpoints
- **Desktop**: > 768px
- **Tablet**: ≤ 768px
- **Mobile**: ≤ 480px
- **Small Mobile**: ≤ 320px

## 🏗 Project Structure

```
sudoku_solver/
├── src/
│   ├── main/
│   │   ├── java/com/jbme/sudoku_solver/
│   │   │   ├── SudokuSolverApplication.java    # Main Spring Boot application
│   │   │   ├── SudokuController.java           # REST API controller
│   │   │   ├── SudokuSolver.java              # Core solving algorithm
│   │   │   ├── Grid.java                      # Grid data model
│   │   │   └── SudokuResponse.java            # API response model
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html                 # Frontend UI
│   │       │   ├── styles.css                 # Responsive styling
│   │       │   └── sudoku.js                  # Frontend logic
│   │       └── application.properties         # Spring configuration
│   └── test/                                  # Unit and integration tests
├── .github/workflows/                         # CI/CD pipeline
├── testGrid/                                  # Sample Sudoku puzzles
├── Dockerfile                                 # Container configuration
├── render.yaml                               # Render deployment config
└── pom.xml                                   # Maven dependencies
```

## 🔧 Configuration

### Application Properties

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/

# Actuator endpoints
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized

# Logging
logging.level.com.jbme.sudoku_solver=INFO
```

### Environment Variables (Production)

- `JAVA_OPTS`: JVM configuration options
- `SPRING_PROFILES_ACTIVE`: Active Spring profiles
- `PORT`: Application port (auto-configured on Render)

## 🚀 Deployment on Render

The application is configured for automatic deployment on Render:

1. **Automatic Builds**: Triggered on pushes to `Sudoku_Solver_web` branch
2. **Health Checks**: Configured at `/actuator/health`
3. **Resource Optimization**: 512MB memory limit with container support
4. **Production Profile**: Optimized logging and performance settings

### Manual Deployment Steps

1. Fork/clone this repository
2. Connect your GitHub repository to Render
3. Create a new Web Service
4. Use the following settings:
   - **Environment**: Docker
   - **Dockerfile Path**: `./Dockerfile`
   - **Health Check Path**: `/actuator/health`
   - **Branch**: `Sudoku_Solver_web`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Render for reliable cloud hosting
- GitHub Actions for seamless CI/CD
- The open source community for inspiration and best practices

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/jbmey/sudoku_solver/issues) page
2. Create a new issue with detailed information
3. Check application logs via `/actuator/health` endpoint

---

**Happy Sudoku Solving! 🧩**