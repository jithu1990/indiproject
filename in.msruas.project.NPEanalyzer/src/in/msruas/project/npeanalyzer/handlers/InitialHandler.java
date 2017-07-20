package in.msruas.project.npeanalyzer.handlers;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.swt.widgets.MessageBox;

import in.msruas.project.npeanalyzer.logic.JlistConstructor;

public class InitialHandler extends AbstractHandler {
	ExecutionEvent event=null;
    private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
    ArrayList<String> methodNames=new ArrayList<>();
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	this.event=event;
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        // Get all projects in the workspace
        IProject[] projects = root.getProjects();
        // Loop over all projects
        for (IProject project : projects) {
            try {
                if (project.isNatureEnabled(JDT_NATURE)) {
                    analyseMethods(project);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void analyseMethods(IProject project) throws JavaModelException, ExecutionException {
        IPackageFragment[] packages = JavaCore.create(project)
                .getPackageFragments();
        String projName=project.getLocation().toString();
        // parse(JavaCore.create(project));
        for (IPackageFragment mypackage : packages) {
            if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                createAST(mypackage,projName);
            }

        }
    }

    private void createAST(IPackageFragment mypackage, String projName)
            throws JavaModelException, ExecutionException {
        for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
            // now create the AST for the ICompilationUnits
        	
            CompilationUnit parse = parse(unit);
            MethodVisitor visitor = new MethodVisitor();
           
            parse.accept(visitor);
            
            for (MethodDeclaration method : visitor.getMethods()) {
                System.out.println("Method name: " + method.getName()
                        + " Return type: " + method.getReturnType2());
                methodNames.add( projName+"$"+method.getName().toString());
                
                
            }
            JlistConstructor jlist=new JlistConstructor(methodNames);
            jlist.runList(methodNames,event);            

        }
    }

    /**
     * Reads a ICompilationUnit and creates the AST DOM for manipulating the
     * Java source file
     *
     * @param unit
     * @return
     */

    private static CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null); // parse
    }
}