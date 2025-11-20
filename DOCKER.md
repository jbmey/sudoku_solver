# Docker Deployment Guide

This guide explains how to deploy the Sudoku Solver application using Docker, specifically optimized for Render hosting.

## Files Overview

- **`Dockerfile`**: Multi-stage Docker build optimized for production
- **`.dockerignore`**: Excludes unnecessary files from build context
- **`render.yaml`**: Render-specific deployment configuration

## Local Docker Testing

### Build the image:
```bash
docker build -t sudoku-solver .
```

### Run locally:
```bash
docker run -d -p 8080:8080 -e PORT=8080 --name sudoku-app sudoku-solver
```

### Test the application:
```bash
# Check health endpoint
curl http://localhost:8080/actuator/health

# Access the application
curl http://localhost:8080
```

## Render Deployment

### Option 1: Using render.yaml (Recommended)
1. Push your code to GitHub
2. Connect your GitHub repository to Render
3. Render will automatically detect the `render.yaml` file
4. Deploy with one click

### Option 2: Manual Render Configuration
1. Create a new Web Service on Render
2. Connect your GitHub repository
3. Configure:
   - **Environment**: Docker
   - **Dockerfile Path**: `./Dockerfile`
   - **Branch**: `Sudoku_Solver_web`
   - **Health Check Path**: `/actuator/health`

## Environment Variables (Optional)

Set these in Render dashboard for optimization:
- `JAVA_OPTS`: `-Xmx512m -XX:+UseContainerSupport`
- `SPRING_PROFILES_ACTIVE`: `production`

## Docker Image Features

- **Multi-stage build**: Smaller final image size
- **Security**: Runs as non-root user
- **Health checks**: Built-in health monitoring
- **Optimized JVM**: Container-aware memory settings
- **Signal handling**: Proper shutdown with dumb-init
- **Compression**: Gzip compression enabled

## Troubleshooting

### Build Issues
- Ensure Java 21 is specified correctly
- Check that all dependencies are available
- Verify `pom.xml` is valid

### Runtime Issues
- Check logs: `docker logs <container-name>`
- Verify health endpoint: `/actuator/health`
- Ensure PORT environment variable is set correctly

### Render-Specific
- Health check must return 200 OK
- Application must bind to `0.0.0.0:$PORT`
- Render provides the PORT environment variable automatically