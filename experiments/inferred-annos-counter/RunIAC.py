import subprocess
import glob
import os
import sys
import pandas as pd

project_name = str(sys.argv[1])


annotationsTable = pd.read_csv("inputExamples/"+project_name+"/AnnotationStats.csv")
annotationsTable["Names"] = ""

for index, row in annotationsTable.iterrows():
    name = row["Annotation"].split(".")[-1]
    annotationsTable.at[index, 'Names'] = name

    

def get_all_files(path, extension):
    # path = r'inputExamples/plume-util/human-written/'
    # extension = '.java'
    file_list = []
    for root, dirs_list, files_list in os.walk(path):
        for file_name in files_list:
            if os.path.splitext(file_name)[-1] == extension:
                file_name_path = os.path.join(root, file_name)
                file_list.append(os.path.abspath(file_name_path))
    return file_list
    
javafiles = get_all_files('inputExamples/'+project_name+'/human-written/', '.java')
ajavafiles = get_all_files('inputExamples/'+project_name+'/generated/', '.ajava')


if os.path.exists("inputExamples/"+project_name+"/output") == 0:
    os.system("mkdir "+"inputExamples/"+project_name+"/output")


for jf in javafiles:
    jf_name = jf[:-5]
    jf_name = jf_name.split("/")[-1]
    ajf_list = []
    
    for ajf in ajavafiles:
        ajf_name = ajf[:-6].split("/")[-1]
        if len(jf_name) == len(ajf_name):
            continue
        if jf_name in ajf_name and ajf_name[0:len(jf_name)] == jf_name:
            ajf_list.append(ajf)
    if len(ajf_list) == 0:
        continue
    ajf_all = ' '.join(ajf_list)
    
    outputFileName = "inputExamples/"+project_name+"/output/"+jf_name+".txt"
    process = subprocess.run("./gradlew run --args='"+ jf +" " + ajf_all + "' >> " + outputFileName, shell=True)
    
    f = open(outputFileName, "r")
    for x in f:
      if "@" in x:
          lineElements = x.split(" ")
          annot = lineElements[0][1:]
          stats = lineElements[2].split("/")
          found = stats[0]
          original = stats[1]
          
          if annotationsTable[annotationsTable["Names"]==annot].empty:
              print(annot, annotationsTable[annotationsTable["Names"]==annot])
              print()
              continue
          annotationsTable.loc[annotationsTable["Names"]==annot, "Original inferred count"] = int(found) + int(annotationsTable.loc[annotationsTable["Names"]==annot, "Original inferred count"].values[0])


annotationsTable = annotationsTable.drop('Names', axis=1)
annotationsTable.to_csv("inputExamples/"+project_name+"/AnnotationStats.csv", index=False)          






