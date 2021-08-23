echo off
set project_path=%1
set output_path=%2

java -jar RabbitMQMigrator-1.0.0.jar %project_path% %output_path%
