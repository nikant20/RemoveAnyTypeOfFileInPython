import os

for parent, dirnames, filenames in os.walk('F:\\hibernate-jpa-tutorial-for-beginners-in-100-steps'):
    for fn in filenames:
        if fn.lower().endswith('.srt'):
            os.remove(os.path.join(parent, fn))
