package faust

import scalax.collection._
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.io.json._
import scalax.collection.io.json.descriptor.predefined.{Di}

import net.liftweb.json._

import scala.collection.immutable.Set
import scala.io.Source

sealed trait System
case class SystemFeature(name: String) extends System

object DependencyChecker {
  private val systemFeatureDescriptor = new NodeDescriptor[SystemFeature](typeId = "SystemFeatures") {
    def id(node: Any) = node match {
      case SystemFeature(name) => name
    }
  }

  private val systemDescriptor = new Descriptor[System](
    defaultNodeDescriptor = systemFeatureDescriptor,
    defaultEdgeDescriptor = Di.descriptor[System]()
  )

  private val root = SystemFeature("Base System")

  private val systemFeatureFilename = "features.json"

  private val systemFeatureImport = Source.fromFile(systemFeatureFilename).getLines.mkString
  private val dependencyGraph = Graph.fromJson[System, DiEdge](systemFeatureImport,systemDescriptor)

  //not very functional, but object oriented
  private val systemFeatureSet = scala.collection.mutable.LinkedHashSet[SystemFeature]()

  def apply(requestFilename: String = "request.json"): List[String] = {
    val requestImport = Source.fromFile(requestFilename).getLines.mkString
    val requestGraph = Graph.fromJson[System, DiEdge](requestImport,systemDescriptor)
    systemFeatureSet.clear()

    for(f <- requestGraph.nodes) {
      systemFeatureSet ++= getDependencies(f.toOuter.asInstanceOf[SystemFeature])
    }

    for (f <- systemFeatureSet.toList) yield f.name
  }

  private def getDependencies(child: SystemFeature): scala.collection.mutable.LinkedHashSet[SystemFeature] = {
    //only go to the trouble of finding the dependencies if we haven't already seen them
    if(child != root && !systemFeatureSet.contains(child)) {
        //find our parents
        for(p <- (dependencyGraph get child).diSuccessors) {
          systemFeatureSet ++= getDependencies(p.toOuter.asInstanceOf[SystemFeature])
        }
        //we depend on ourself, so add us too
        systemFeatureSet += child
    } else {
      systemFeatureSet += child
    }
  }

}
