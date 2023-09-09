import os

def delete_empty_directories(directory_path):
    for root, dirs, files in os.walk(directory_path, topdown=False):
        for directory in dirs:
            folder_path = os.path.join(root, directory)
            if not os.listdir(folder_path):
                os.rmdir(folder_path)
                print(f"Deleted empty directory: {folder_path}")

# Example usage:
directory_path = "D:\\takeout-20230620T022921Z-001\\Takeout"  # Replace with the actual directory path

delete_empty_directories(directory_path)
